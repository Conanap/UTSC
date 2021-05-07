#! usr/bin/python3
# client

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

# sends the strings to the specified socket
def sendSoc(soc, outstr):
  # tell em the size
  soc.send(str(len(outstr)).encode())
  # wait for acknoledgement
  soc.recv(ACK)
  # send the str
  try:
    outstr = outstr.encode()
  except:
    pass
  soc.send(outstr)

def readSoc(soc):
  # read 8 bytes and det size of transmission
  tot = soc.recv(MSG_SIZE)
  tot = int(tot.decode('utf-8'))

  # appreciate connecting party for giving us the size
  soc.send(b'ack')

  ret = b''
  count = 0
  nextsize = tot if tot < 1024 else 1024

  # read until all of indicated transmission size is donezo rapunzel
  while (count != tot):
    string = soc.recv(nextsize)
    ret += string
    count += nextsize

    nextsize = tot - count if tot - count < 1024 else 1024

  # return receivied msg, hash and signature
  try:
    ret = ret.decode()
  except:
    pass
  return (ret, readHash(soc), readSig(soc))

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
  soc.recv(3)
  # send hash
  soc.send(rhash)

# parse incoming transmission into dict
def parseRequest(line):
  if(isinstance(line, bytes)):
    line = line.decode()
  line = line.split(',')
  ret = {}
  for i in range(len(line)):
    ret[line[i].split('=')[0]] = line[i].split('=')[1]
    # sometimes it retains the b' even though it's actually a string???
    if(ret[line[i].split('=')[0]][0:2] == 'b\''):
      ret[line[i].split('=')[0]] = ret[line[i].split('=')[0]][2:-1].replace('\\n', '\n')

  return ret

# used to generate rsa key pair
def genKeys():
  # get me own key
  maCle = RSA.generate(2048)
  privateKey = maCle.exportKey()
  publicKey = maCle.publickey().exportKey()

  return (publicKey, privateKey, maCle)

# used to import other ppls keys
def importKey(autreCles, ident, cle):
  autreCles[ident] = RSA.importKey(cle)

def computeHash(msg):
  hasher = SHA256.new()
  if(not isinstance(msg, bytes)):
    msg = msg.encode()
  hasher.update(msg)
  mhash = hasher.digest()
  return mhash

# verify the incoming transmission
def verifyComs(msg, ident, rhash, signature):
  # check hash matches
  mhash = computeHash(msg)

  # verify signature
  if(not autreCles[ident].verify(rhash, signature) or mhash != rhash):
    return False

  return True

# uploads own key to PKS (usually own pk)
def uploadKey(publicKey):
  publicKey = str(publicKey)

  # send the set request
  sendstr = 'ops=set,ident=alice,pk=' + publicKey
  soc = socket.socket()
  soc.connect(('localhost', PKS_PORT))
  sendSoc(soc, sendstr)
  soc.shutdown(socket.SHUT_RDWR)
  soc.close()

# used to get PKS' key
# for all intents and purposes pretend this was taken through a physical, secure channel
def getPKSKey(autreCles):
  s = socket.socket()
  s.connect(('localhost', PKS_PORT))
  starter = 'ident=alice,'

  outstr = starter + 'ops=get,gident=pks'
  sendSoc(s, outstr)

  (read, rhash, signature) = readSoc(s)

  if(read == '400'):
    perror("PKS returned 400, check request format")
  elif(read == '401'):
    perror("Bob's key cannot be found, try again later")

  params = parseRequest(read)
  importKey(autreCles, params['ident'], params['pk'])

# generates an 8 char nonce
def genNonce():
  return get_random_bytes(8)

def genSeshKey():
  return get_random_bytes(256)

# THE FOLLOWING 2 FUNCTION REFERENCES CODE FROM
# https://pycryptodome.readthedocs.io/en/latest/src/examples.html
# encrypts a given message
def encryptRSAMsg(key, msg):
  return ''

def decryptRSAMsg(key, msg):
  pass

# start the initial connection
def initConnect(soc, ident, host, maCle, autreCles):
  # get our boi's key
  key = autreCles[ident]
  # gen a new nonce
  nonce = genNonce()
  # request str
  outstr = 'ident=alice,nonce=' + str(nonce) + ',type=init'
  # encrypt using target's public key
  encrypted = key.encrypt(outstr.encode(), Random.new().read)[0] # tuple of string and nothing
  ehash = computeHash(outstr)
  # sign - tuple of string and nothing
  signature = maCle.sign(ehash, Random.new().read)[0]

  # send encrypted stuff
  sendSoc(soc, encrypted)
  sendSocHash(soc, ehash)
  sendSocSig(soc, signature)

  return nonce

# verify the reply to the initial connection has the correct nonce
def verifyReply(soc, ident, maCle, mnonce):
  (msg, mhash, signature) = readSoc(soc)

  msg = maCle.decrypt(msg)

  # verify integrity
  if(not verifyComs(msg, ident, mhash, signature)):
    perror('Error: Invalid signature or mismatching hash')
    exitCS(soc)
    exit(1)

  msg = parseRequest(msg)
  # check if nonce is correct
  # so msg nonce is 1. str not byte
  # 2. has extra escapes in byte form
  # so we have to remove those
  ynonce = msg['yournonce'].encode()#.replace(b'\\\\', b'\\')
  print(mnonce, ynonce)
  if(ynonce != mnonce):
    perror('Error: Nonce mismatch')
    exitCS(soc)
    exit(1)

  return msg['nonce']

# start comms by trying to establish a session key
def commenceComs(soc, ident, nonce):
  seshKey = genSeshKey()
  key = autreCles[ident]

  eSeshKey = key.encrypt(seshKey.encode(), Random.new().read)
  outstr = 'ident=alice,yournonce=' + nonce + ',eseshKey=' + eSeshKey
  khash = computeHash(outstr)
  signature = maCle.sign(khash, Random.new().read)

  sendSoc(eSeshKey)
  sendSocHash(cs, khash)
  sendSocSig(cs, signature)

  return seshKey

# just waiting for bob to say hi encrypted in seshkey
def waitFinalReply(soc, seshKey, maCle):
  # the third thing they send after establishing sesh key is actually a nonce
  # nonce for aes
  (msg, mhash, nonce) = readSoc(soc)

  # decrypt
  aesCipher = AES.new(seshKey, AES.MODE_EAX, nonce)
  decrypted = aesCipher.decrypt_and_verify(decrypted)

  params = parseRequest(decrypted)

  if(params['msg'] == 'hello'):
    return True

  perror('Error: Invalid session start message.')
  exitCS(soc)
  exit(1)

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
  importKey(autreCles, ident, params['pk'].encode())




if __name__ == '__main__':

  # init
  autreCles = {}
  maCle = None

  # generate a new key pair, then upload it to public server
  (publicKey, privateKey, maCle) = genKeys()

  if(not publicKey or not privateKey or not maCle):
    perror('Error: Cannot generate key')
    exit(1)

  status = uploadKey(publicKey)
  # we need the pks' pk
  # pretend somehow we got it through a physical, secure channel
  # just for the purposes of the thiss exercise we don't LUL
  getPKSKey(autreCles)


  # part 1: demonstrating upload to bob
  getKeyFromPKS('bob', autreCles)

  # try to start comms with bob
  # hard coded port
  s = socket.socket()

  # connect to our boi bob
  s.connect(('localhost', BOB_PORT))
  nonce = initConnect(s, 'bob', 'localhost', maCle, autreCles)
  nonce = verifyReply(s, 'bob', maCle, nonce)
  seshKey = commenceComs(s, 'bob', nonce)
  waitFinalReply(s, seshKey, maCle)
  print('success')
  # if you reached here, connection success! Use seshKey to encrypt instead

  # upload a file