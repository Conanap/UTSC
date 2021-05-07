#!/usr/local/bin/python3

from Crypto.Cipher import ARC4
import wave

# =============================================
# ========= write your code below  ============
# =============================================

def encrypt(keyFile, inputFile, outputFile):
    ''' 
    Encrypts the wave inputFile with the keyFile using the rc4 cipher (from PyCryptodome)
    and writes the wave outputFile
    The wave outputFile must be a playable wave file.
    (string, string, string) -> None
    '''
    
    inWave = wave.open(inputFile, 'rb')
    keyfp = open(keyFile, 'r')
    key = keyfp.read()
    keyfp.close()

    key = key.encode('utf-8')
    cipher = ARC4.new(key)

    numFrame = inWave.getnframes()
    header = inWave.getparams()

    frameObj = inWave.readframes(numFrame)
    ret = cipher.encrypt(frameObj)

    outWave = wave.open(outputFile, 'wb')
    outWave.setparams(header)
    outWave.writeframes(ret)
    
def decrypt(keyFile, inputFile, outputFile):
    ''' 
    Decrypts the wave inputFile with the keyFile using the rc4 cipher (from PyCryptodome)
    and writes the wave wave outputFile
    The wave output file must be a playable wave file. 
    (string, string, string) -> None
    '''
    
    inWave = wave.open(inputFile, 'rb')
    keyfp = open(keyFile, 'r')
    key = keyfp.read()
    keyfp.close()

    key = key.encode('utf-8')
    cipher = ARC4.new(key)

    numFrame = inWave.getnframes()
    header = inWave.getparams()

    frameObj = inWave.readframes(numFrame)
    ret = cipher.decrypt(frameObj)

    outWave = wave.open(outputFile, 'wb')
    outWave.setparams(header)
    outWave.writeframes(ret)

# =============================================
# ===== do not modify the code below ==========
# =============================================
    
if __name__ == "__main__":
    import os, sys, getopt
    def usage():
        print ('Usage:    ' + os.path.basename(__file__) + ' options input_file ')
        print ('Options:')
        print ('\t -e, --encrypt')
        print ('\t -d, --decrypt')
        print ('\t -k key_file, --key=key_file')
        print ('\t -o output_file, --output=output_file')
        sys.exit(2)
    try:
      opts, args = getopt.getopt(sys.argv[1:],"hedk:o:",["help", "encrypt", "decrypt", "key=", "output="])
    except getopt.GetoptError as err:
      print(err)
      usage()
    # extract parameters
    mode = None
    keyFile = None
    outputFile = None
    inputFile = args[0] if len(args) > 0 else None
    for opt, arg in opts:
        if opt in ("-h", "--help"):
           usage()
        elif opt in ("-e", "--encrypt"):
           mode = encrypt
        elif opt in ("-d", "--decrypt"):
           mode = decrypt
        elif opt in ("-k", "--key"):
           keyFile = arg
        elif opt in ("-o", "--output"):
           outputFile = arg
    # check arguments
    if (mode is None):
       print('encrypt/decrypt option is missing\n')
       usage()
    if (keyFile is None):
       print('key option is missing\n')
       usage()
    if (outputFile is None):
       print('output option is missing\n')
       usage()
    if (inputFile is None):
       print('input_file is missing\n')
       usage()
    # run the command
    mode(keyFile, inputFile, outputFile)
