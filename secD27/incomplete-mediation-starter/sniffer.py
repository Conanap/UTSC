#!/usr/local/bin/python3

from scapy.all import *
from scapy_http.http import *
from urllib.parse import urlparse, parse_qs, unquote
import sys

# =============================================
# ========= write your code below  ============
# =============================================

def packet_filter(packet):
    return packet.haslayer(HTTPRequest) or packet.haslayer(HTTPResponse)

def getResponse(packet, attr):
    return getattr(packet[HTTPResponse], attr).decode()

def packet_process(packet):
    # packet.show()
    if(packet.haslayer(HTTPRequest)):
      print('[HTTPRequest] ' + packet[HTTPRequest].Host.decode("utf-8") +  " " + packet[HTTPRequest].Method.decode("utf-8") + " " + packet[HTTPRequest].Path.decode("utf-8").split('?')[0])
      try:
        packet[HTTPRequest].Cookie
        temp = packet[HTTPRequest].Cookie.decode().split(';')
        if(len(temp) > 0):
          print('cookies:')
        for i in temp:
          print('  ' + i.split('=')[0].strip() + ': ' + unquote(i.split('=')[1]).strip())
      except:
        pass
      try:
        temp = packet['Raw'].load
        temp = packet['Raw'].load.decode().split('&')
        if(len(temp) > 0):
          print('body:')
        for i in temp:
          print('  ' + i.split('=')[0] + ': ' + unquote(i.split('=')[1]))
      except:
        pass
    elif(packet.haslayer(HTTPResponse)):
      print('[HTTPResponse] ' + getResponse(packet, 'Status-Line').split(' ')[1])
      print('body: ' + packet['Raw'].load.decode())
    print('*****')


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