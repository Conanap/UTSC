#! usr/bin/python3
# server side

import socket
import sys
from Crypto.PublicKey import RSA
from Crypto.Hash import SHA256
from Crypto.Random import get_random_bytes
from Crypto import Random
from Crypto.Cipher import AES, PKCS1_OAEP

# CONSTS
BOB_PORT = 8000
PKS_PORT = 8001
ACK = 3
MSG_SIZE = 8
HASH_SIZE = 4
SIG_SIZE = 3

# closes connection
def exitCS(cs):
  cs.shutdown(socket.SHUT_RDWR)
  cs.close()

def perror(string):
  print(string, file=sys.stderr)

# helper functions to read and write
# send str to target socket
def sendSoc(soc, outstr):
  # send size of trans
  soc.send(str(len(outstr)).encode())
  # wait for ack
  soc.recv(ACK)
  # send msg
  if(not isinstance(outstr, bytes)):
    outstr = outstr.encode()
  soc.send(outstr)

# send signature to target socket
def sendSocSig(soc, sig):
  recv = ''
  # wait for them to be ready
  while recv != 'sig':
    recv = soc.recv(SIG_SIZE).decode()

  # send size
  soc.send(str(len(str(sig))).encode())
  soc.recv(ACK) # ack
  # convert to str
  sig = str(sig).encode()
  # send
  soc.send(sig)

# send hash to target socket
def sendSocHash(soc, rhash):
  recv = ''
  # wait for them to be ready
  while recv != 'hash':
    recv = soc.recv(HASH_SIZE).decode()

  # send size
  soc.send(str(len(str(rhash))).encode())
  # ack
  soc.recv(ACK)
  # send hash
  soc.send(rhash)

# read from socket for msg, hash and sig
def readSoc(soc):
  # read 8 bytes and det size of transmission
  tot = soc.recv(MSG_SIZE)
  tot = int(tot.decode('utf-8'))

  # appreciate connecting party for giving us the size
  soc.send(b'ack')

  ret = b''
  count = 0
  nextsize = tot if tot < 1024 else 1024

  # read until all is read
  while (count != tot):
    string = soc.recv(nextsize)
    ret += string
    count += nextsize

    nextsize = tot - count if tot - count < 1024 else 1024

  # return receivied msg, hash and signature
  try:
    ret = ret.decode()
  except:
    pass # if it can't be decoded yet, probably encrypted
  return (ret, readHash(soc), readSig(soc))

# parse incoming transmission into dict
def parseRequest(line):
  line = line.split(',')
  ret = {}
  for i in range(len(line)):
    ret[line[i].split('=')[0]] = line[i].split('=')[1]
    # sometimes it retains the b' even though it's actually a string???
    # and it converts \n to escaped \n?????? why??
    if(ret[line[i].split('=')[0]][0:2] == 'b\''):
      ret[line[i].split('=')[0]] = ret[line[i].split('=')[0]][2:-1].replace('\\n', '\n')

  return ret

# get transmission signature from server
def readSig(s):
  # ask for signature
  s.send(b'sig')
  # figure out signature size
  tot = int(s.recv(MSG_SIZE).decode())
  # ack the size
  s.send(b'ack')
  # sigatures are tuple pairs of int with nothing
  # pycrypto RSA is weird
  return (int(s.recv(tot)),) # signatures are relatively small

# get tramission hash from server
def readHash(s):
  # ask for hash
  s.send(b'hash')
  # figure out size of hash
  tot = int(s.recv(MSG_SIZE).decode())
  # ack size
  s.send(b'ack')
  # hash is byte obj don't need to conv
  return s.recv(tot) # hashes are also relatively small


def parseAuth(params):
  try:
    ret = (params['ident'], params['nounce'], params['signature'])
  except:
    ret = (None, None, None)
  return ret

# used to import other ppls keys
def importKey(autreCles, ident, cle):
  autreCles[ident] = RSA.importKey(cle)

def computeHash(msg):
  hasher = SHA256.new()
  hasher.update(msg.encode())
  mhash = hasher.digest()
  return mhash

# verify the incoming transmission
def verifyComs(msg, ident, rhash, signature):
  # check hash matches
  mhash = computeHash(msg)

  # check hash matches
  if(not autreCles[ident].verify(rhash, signature) or mhash != rhash):
    return False

  return True

# CODE REFERENCED FROM https://pycryptodome.readthedocs.io/en/latest/src/examples.html
# used to generate rsa key pair
def genKeys():
  maCle = RSA.generate(2048)
  privateKey = maCle.exportKey()
  publicKey = maCle.publickey().exportKey()

  return (publicKey, privateKey, maCle)

# uploads a key to PKS (usually own pk)
def uploadKey(publicKey):
  publicKey = str(publicKey)

  sendstr = 'ops=set,ident=bob,pk=' + publicKey
  soc = socket.socket()
  soc.connect(('localhost', PKS_PORT))
  soc.send(str(len(sendstr)).encode())
  soc.recv(ACK)
  soc.send(sendstr.encode())
  soc.shutdown(socket.SHUT_RDWR)
  soc.close()

# used to get PKS' key
# for all intents and purposes pretend this was taken through a physical, secure channel
def getPKSKey(autreCles):
  s = socket.socket()
  s.connect(('localhost', PKS_PORT))
  starter = 'ident=bob,'

  outstr = starter + 'ops=get,gident=pks'
  sendSoc(s, outstr)

  (read, rhash, signature) = readSoc(s)

  if(read == '400'):
    perror("PKS returned 400, check request format")
    exitCS(s)
    exit(1)
  elif(read == '401'):
    perror("PKS's key cannot be found, try again later")
    exitCS(s)
    exit(1)

  params = parseRequest(read)
  importKey(autreCles, params['ident'], params['pk'])

# generates an 8 char nonce
def genNonce():
  return get_random_bytes(8)

# THE FOLLOWING FUNCTION REFERENCES CODE FROM
# https://pycryptodome.readthedocs.io/en/latest/src/examples.html
# encrypts a given message
def encryptMsg(key, msg):
  return ''

def replyInitConnect(soc, ident, ononce, maCle, autreCles):
  # get our boi's key
  key = autreCles[ident]
  # gen a new nonce
  nonce = genNonce()
  # request str
  outstr = 'ident=bob,nonce=' + str(nonce) + ',type=init,' + 'yournonce=' + ononce
  # encrypt using target's public key
  encrypted = key.encrypt(outstr.encode(), Random.new().read)[0] # tuple of string and nothing
  ehash = computeHash(outstr)
  # sign - tuple of string and nothing
  signature = maCle.sign(ehash, Random.new().read)[0]

  # send encrypted stuff
  sendSoc(cs, encrypted)
  sendSocHash(cs, ehash)
  sendSocSig(cs, signature)

  return nonce

def replyAndVerifyCommenceComs(soc, ident, nonce, autreCles):
  key = autreCles[ident]
  (msg, mhash, signature) = readSoc(soc)
  if(computeHash(msg) != mhash or autreCles[ident].verify(mhash, signature)):
    perror("Error: Mismatched hash or invalid signature")
    exitCS(soc)
    exit(1)

  params = parseRequest(msg)

  if(params['yournonce'] != nonce):
    perror("Error: Nonce mismatch.")
    exitCS(soc)
    exit(1)

  eseshKey = params['eseshKey']
  seshKey = key.decrypt(eseshKey)

  outstr = 'ident=bob,msg=hello'

  aesCipher = AES.new(seshKey, AES.MODE_EAX)
  nonce = aesCipher.nonce
  (encrypted, mhash) = aesCipher.encrypt_and_digest(outstr)

  sendSoc(encrypted)
  sendSocHash(mhash)
  sendSocSig(nonce)

  return seshKey

# retrives a key of another user from pks
def getKeyFromPKS(ident, autreCles):
  s = socket.socket()
  s.connect(('localhost', PKS_PORT))
  outstr = 'ident=bob,gident=' + ident + ',ops=get'
  sendSoc(s, outstr)

  (read, rhash, signature) = readSoc(s)
  if(not autreCles['pks'].verify(rhash, signature) or rhash != computeHash(read)):
    perror('Error: Mismatched hash or invalid signature.')
    exitCS(s)
    exit(1)

  if(read == '404'):
    perror('Error: ' + ident + "'s key cannot be found, try again later.")

  params = parseRequest(read)
  importKey(autreCles, ident, params['pk'])

if __name__ == "__main__":
  # init
  autreCles = {}
  # generate a new key pair, then upload it to public server
  (publicKey, privateKey, maCle) = genKeys()
  status = uploadKey(publicKey)
  # we need the pks' pk
  # pretend somehow we got it through a physical, secure channel
  # just for the purposes of the thiss exercise we don't LUL
  getPKSKey(autreCles)

  # create the socket for rest of the programme
  s = socket.socket()
  s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

  # THE FOLLOWING CODE WAS WRITTEN IN REFERENCE TO
  # https://docs.python.org/3/howto/sockets.html
  # CODE WRITTEN USING THIS REFERENCE LAST UNTIL OTHERWISE STATED
  # note: any code similar is also isnpred by the above holy document

  # bind to local host
  s.bind(('', BOB_PORT))
  # start listening
  s.listen()

  # run the server
  while True:
    (cs, addr) = s.accept()
    # END OF REFERENCED CODE

    # start accepting transmission / request
    (instr, rhash, signature) = readSoc(cs)
    # since it's init trasmit, params must include ident and nounce
    # rest is ignored
    # decrypt first
    instr = maCle.decrypt(instr).decode()
    params = parseRequest(instr)

    # terminate transmissions if it's bad
    if('nonce' not in params.keys()):
      perror('Error: Bad handshake')
      sendSoc(cs, '400')
      exitCS(cs)
      continue

    # the nonce for this session
    nonce = params['nonce']

    # can't figure who this it
    # shutting it down boi
    if params['ident'] == None:
      perror('Conenction terminated: Unkown identity')
      exitCS(cs)
      continue

    getKeyFromPKS(params['ident'], autreCles)
    nonce = replyInitConnect(cs, params['ident'], nonce, maCle, autreCles)
    seshKey = replyAndVerifyCommenceComs(cs, params['ident'], nonce, autreCles)

    # ended
    exitCS(cs)