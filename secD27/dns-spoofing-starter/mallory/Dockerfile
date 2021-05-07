FROM ubuntu:latest

RUN apt-get update && apt-get install dnsutils dsniff iptables net-tools python python3 wget libdnet python3-pip git tcpdump -y

RUN pip3 install scapy-python3
RUN pip3 install scapy-http