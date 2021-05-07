#!/usr/local/bin/python3

# =============================================
# ========= write your code below  ============
# =============================================
def rc4(key, inputStream):
    ''' returns the RC4 encoding of inputStream based on the key
    (bytes, bytes) -> bytes
    '''
    seed = [None] * 256
    length = len(key)
    j = 0

    for i in range(256):
      if(seed[i] is None):
        seed[i] = i
      j = (j + seed[i] + key[i % length]) % 256
      if(seed[j] is None):
        seed[j] = j
      (seed[i], seed[j]) = (seed[j], seed[i])

    i = 0
    j = 0
    ret = []
    byte = None
    length = len(inputStream)

    for k in range(length):
      i = (i + 1) % 256
      j = (j + seed[i]) % 256
      (seed[i], seed[j]) = (seed[j], seed[i])

      byte = seed[(seed[i] + seed[j]) % 256]

      ret.append(byte ^ inputStream[k])

    ret = bytes(ret)
    return ret

# =============================================
# ===== do not modify the code below ==========
# =============================================
    
if __name__ == "__main__":
    import os, sys, getopt
    def usage():
        print ('Usage:    ' + os.path.basename(__file__) + ' options input_file ')
        print ('Options:')
        print ('\t -k key_file, --key=key_file')
        print ('\t -o output_file, --output=output_file')
        sys.exit(2)
    try:
      opts, args = getopt.getopt(sys.argv[1:],"hk:o:",["help", "key=", "output="])
    except getopt.GetoptError as err:
      print(err)
      usage()
    # extract parameters
    keyFile = None
    outputFile = None
    inputFile = args[0] if len(args) > 0 else None
    for opt, arg in opts:
        if opt in ("-h", "--help"):
           usage()
        elif opt in ("-k", "--key"):
           keyFile = arg
        elif opt in ("-o", "--output"):
           outputFile = arg
    # check arguments
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
    with open(keyFile, "rb") as keyStream:
        key = keyStream.read()
        with open(inputFile, "rb") as inputStream:
            data = inputStream.read()
            output = rc4(key, data)
            with open(outputFile, "wb") as outputStream:
                outputStream.write(output)