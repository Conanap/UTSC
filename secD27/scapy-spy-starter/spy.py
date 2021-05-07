#!/usr/local/bin/python3

from scapy.all import *
from scapy_http.http import *
import sys

# =============================================
# ========= write your code below  ============
# =============================================

def packet_filter(packet):
    ptype = packet_type(packet)
    if(not ptype):
        return False

    ret = ptype == 'TCP' and packet[TCP].dport == 443
    ret = ret or ptype == 'TCP' and packet[TCP].dport == 80 and packet.haslayer(HTTPRequest)
    ret = ret or ptype == 'UDP' and packet[UDP].dport == 53
    return ret

def packet_type(packet):
    if(packet.haslayer(TCP)):
        return 'TCP'
    elif(packet.haslayer(UDP)):
        return 'UDP'
    else:
        return None

def packet_process(packet):
    ptype = packet_type(packet)

    sip = packet[IPv6].src if (IPv6 in packet) else packet[IP].src
    dip = packet[IPv6].dst if (IPv6 in packet) else packet[IP].dst

    if(ptype == 'UDP'):
        print('[DNS] ' + sip + ' -> ' + dip + ' ' + packet[DNS].qd.qname.decode("utf-8"))
        pass
    elif(packet[TCP].dport == 443):
        print('[HTTPS] ' + sip + ' -> ' + dip)
        pass
    elif(packet[TCP].dport == 80):
        print('[HTTP] ' + sip + ' -> ' + dip + ' (' + packet[HTTPRequest].Host.decode("utf-8") + ") " + packet[HTTPRequest].Method.decode("utf-8") + " " + packet[HTTPRequest].Path.decode("utf-8"))

def run(count):
    sniff(iface="eth0", lfilter=packet_filter, prn=packet_process, count=count)

# =============================================
# ===== do not modify the code below ==========
# =============================================
    
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