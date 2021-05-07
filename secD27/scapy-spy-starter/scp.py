#!/usr/local/bin/python3

from scapy.all import *
from scapy_http.http import *
import sys

def packet_filter(packet):
    return packet.haslayer(TCP) or packet.haslayer(UDP)

def packet_type(packet):
    if(packet.haslayer(TCP)):
        return 'TCP'
    else:
        return 'UDP'

def packet_process(packet):
    ptype = packet_type(packet)

    sip = packet[IPv6].src if (IPv6 in packet) else packet[IP].src
    dip = packet[IPv6].dst if (IPv6 in packet) else packet[IP].dst
    # sport = str(packet[TCP].sport)
    # dport = str(packet[TCP].dport)
    # print("from " + sip + ":" + sport + " to " + dip + ":" + dport)
    if(ptype == 'TCP' and packet[TCP].dport == 443):
        pass
        print('[HTTPS] ' + sip + ' -> ' + dip)
    elif(ptype == 'TCP' and packet[TCP].dport == 80):
        print('[HTTP] ' + sip + ' -> ' + dip)
        pass
    elif(ptype == 'UDP' and packet[UDP].dport == 53):
        print('[DNS] ' + sip + ' -> ' + dip)


sniff(iface="eth0",  lfilter=packet_filter, prn=packet_process, count=60)