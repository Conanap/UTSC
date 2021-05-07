#!/usr/local/bin/python3

from scapy.all import *
from scapy_http.http import *
from urllib.parse import urlparse, parse_qs
import sys

# =============================================
# ========= write your code below  ============
# =============================================

def packet_filter(packet):
    a = packet.haslayer(HTTPRequest)
    b = packet.haslayer(HTTPResponse)
    return (a or b)

def packet_process(packet):
    # http request
    if (packet.haslayer(HTTPRequest)):
        # host, command, path
        pHost = getattr(packet[HTTPRequest], "Host").decode('utf-8')
        pMetd = getattr(packet[HTTPRequest], "Method").decode('utf-8')
        pPath = getattr(packet[HTTPRequest], "Path").decode('utf-8')
        pPath = pPath.split("?")[0]
        # output
        print ("[HTTPRequest] " + pHost + " " + pMetd + " " + pPath)
        # look for cookies
        cookies = getattr(packet[HTTPRequest], "Cookie")
        if (cookies):
            cookies = cookies.decode('utf-8')
            cookies = cookies.split(";")
            pSess = cookies[0][cookies[0].find("=") + 1:]
            pOrder = cookies[1][cookies[1].find("=") + 1:]
            print("cookies:")
            print("  session-id: " + pSess)
            print("  order-id: " + pOrder)            
        # look for body
        if (packet.haslayer(Raw)):
            body = parse_qs(packet[Raw].load.decode())
            if (body):
                print("body:")
                for aTag in body:
                    print("  " + aTag + ": " + body[aTag][0])            
              #.sprintf("{Raw:%Raw.load%}\n").split(r"\r\n"))
            #body = parse_qs(body)
            #body = body.split("&")
            #keyArr = []
            #tagArr = []
            #for aTag in body:
            #    aTag = aTag.split("=")
            #    keyArr.append(aTag[0])
            #    tagArr.append(aTag[1])
            # cut quotes
            #keyArr[0] = keyArr[0][1:]
            #tagArr[len(tagArr) - 1] = tagArr[len(tagArr) - 1][:-2]
            # output            
    # http response
    if (packet.haslayer(HTTPResponse)):
        # status code
        pStatus = getattr(packet[HTTPResponse], "Status-Line").decode('utf-8')
        pStatus = pStatus[9:13]
        # body // parse_qs due to numbers
        body = ''.join(packet.sprintf("{Raw:%Raw.load%}").split(r"\r\n"))
        body = body[1:-1]
        # output
        print("[HTTPResponse] " + pStatus)
        print("body: " + body)
    # ending
    print("*****")


# packet.haslayer(http.HTTPRequest // .HTTPResponse)
# layer = packet.getlayer(http/HTTP[Request, Response)
# ip = packet.getlayer(IP)

# =============================================
# ===== do not modify the code below ==========
# =============================================

def run(count):
    sniff(iface="eth0", lfilter=packet_filter, prn=packet_process, count=count)

if __name__ == "__main__":
  import os, sys, getopt
  def usage():
       print ('Usage:	' + os.path.basename(__file__) + ' count ')
       sys.exit(2)
  # extract parameters
  try:
     opts, args = getopt.getopt(sys.argv[1:],"h",["help"])
  except getopt.GetoptError as err:
     print(err)
     usage()
     sys.exit(2)
  if not(len(args)== 1):
      print('count argument is missing\n')
      usage()
      sys.exit(2)
  try:
      count = int(args[0]) if len(args) > 0 else 0
  except ValueError:
      print("count must be a natural number")
      sys.exit(2)
  # run the command
  run(count)
