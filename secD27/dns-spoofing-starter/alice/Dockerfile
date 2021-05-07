FROM ubuntu:latest

RUN apt-get update && apt-get install curl dnsutils -y

CMD echo 'nameserver 1.1.1.1' > /etc/resolv.conf ; tail -f /dev/null