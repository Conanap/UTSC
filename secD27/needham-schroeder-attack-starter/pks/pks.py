#! usr/bin/python3
# pkserver side

import socket
import sys
import urllib.parse as parse
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto import Random

# terminates connections
def exitCS(cs):
  cs.shutdown(socket.SHUT_RDWR)
  cs.close()

def perror(string):
  print(string, file=sys.stderr)

# read request for keys
def readSoc(soc):
  # read 4 bytes and det size of transmission
  tot = soc.recv(8)
  tot = int(tot.decode('utf-8'))

  # appreciate connecting party for giving us the size
  soc.send(b'ack')

  ret = b''
  count = 0
  nextsize = tot if tot < 1024 else 1024

  # read until end of msg
  while (count != tot):
    string = soc.recv(nextsize)
    ret += string
    count += nextsize

    nextsize = tot - count if tot - count < 1024 else 1024

  return ret.decode('utf-8')

# send msg to target socket
def sendSoc(soc, outstr):
  # send size
  soc.send(str(len(outstr)).encode())
  # ack
  soc.recv(3)
  # send msg
  soc.send(outstr.encode())

# send signature to target socket
def sendSocSig(soc, sig):
  recv = ''
  # wait for them to be ready
  while recv != 'sig':
    recv = soc.recv(3).decode()

  # send size
  soc.send(str(len(str(sig))).encode())
  soc.recv(3) # ack
  # convert to str
  sig = str(sig).encode()
  # send
  soc.send(sig)

# send hash to target socket
def sendSocHash(soc, rhash):
  recv = ''
  # wait for them to be ready
  while recv != 'hash':
    recv = soc.recv(4).decode()

  # send size
  soc.send(str(len(str(rhash))).encode())
  # ack
  soc.recv(3)
  # send hash
  soc.send(rhash)

# parse msg into dict
def parseRequest(line):
  line = line.split(',')
  ret = {}
  for i in range(len(line)):
    ret[line[i].split('=')[0]] = line[i].split('=')[1]

  return ret

# generate own keys
def genKeys():
  maCle = RSA.generate(2048)
  privateKey = maCle.exportKey()
  publicKey = maCle.publickey().exportKey()

  return (publicKey, privateKey, maCle)

if __name__ == '__main__':

  # local data
  pks = {}

  (publicKey, privateKey, maCle) = genKeys()
  pks['pks'] = publicKey.decode()

  perror('PKS: key gen\'d')
  # create the socket
  s = socket.socket()
  s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)


  # THE FOLLOWING CODE WAS WRITTEN IN REFERENCE TO
  # https://docs.python.org/3/howto/sockets.html
  # CODE WRITTEN USING THIS REFERENCE LAST UNTIL OTHERWISE STATED

  # bind to local host
  s.bind(('', 8001))
  # start listening
  s.listen()

  # run the server
  while True:
    (cs, addr) = s.accept()
    # END OF REFERENCED CODE

    instr = readSoc(cs)
    params = parseRequest(instr)

    try:
      if(params['ops'] == 'set'): # setter
        pks[params['ident']] = params['pk']
        # debug
        perror(params['ident'] + '\'s public key set.')
      elif(params['ops'] == 'get'): # getter
        # if key is DNE, return 404 and terminate connection
        if (params['gident'] not in pks.keys()):
          sendSoc(cs, '404')
          exitCS(cs)
          continue

        # generate msg, sngnature and hash for verification
        hasher = SHA256.new()
        outstr = 'ident=pks,pkid=' + params['gident'] + ',pk=' + pks[params['gident']]
        hasher.update(outstr.encode())
        rhash = hasher.digest()
        signature = maCle.sign(rhash, Random.new().read)[0]

        # send each over
        sendSoc(cs, outstr)
        sendSocHash(cs, rhash)
        sendSocSig(cs, signature)

        # terminate connection
        exitCS(cs)

      else: # invalid operations, send 400
        perror('Error: Unrecognized operation from ' + params['ident'] + ': ' + params['ops'])
        perror("Connection Terminated")
        sendSoc(cs, '400')
        exitCS(cs)
    except KeyError: # no ops param sent, send 400
      perror('Error: Request malformat.')
      perror("Connection terminated")
      sendSoc(cs, '400')
      exitCS(cs)

