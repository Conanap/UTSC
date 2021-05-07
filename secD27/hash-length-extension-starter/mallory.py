import re

from md5p import md5, padding

# =============================================
# ========= write your code below  ============
# =============================================

def forgeIllegalPayload(hmac, extension, keyLength):
    '''
    In this function, you are not to use the secret key shared between alice and bob 
    However, you might know the length of the secret key (keyLength)
    (string, string, integer) -> string
    '''
    arr= hmac.split('$')
    ogHash = arr[1].strip()
    ogMsg = arr[2].strip()

    msg = md5(state=ogHash.decode('hex'), count=512)
    msg.update(extension)
    ogMsg += str(padding(len(ogMsg) * 8 + keyLength * 8))
    ogMsg += extension
    hmac = '$' + msg.hexdigest() + '$' + ogMsg

    return hmac

# =============================================
# ===== do not modify the code below ==========
# =============================================
    
if __name__ == "__main__":
   import os, sys, getopt
   def usage():
        print ('Usage:    ' + os.path.basename(__file__) + ' options input_file ')
        print ('Options:')
        print ('\t -x extension_file, --extension=extension_file')
        print ('\t -o output_file, --output=output_file')
        print ('\t -k n, --key-length=n')
        sys.exit(2)
   try:
      opts, args = getopt.getopt(sys.argv[1:],"hx:o:k:",["help", "extension=", "output=", "key-length="])
   except getopt.GetoptError as err:
      print(err)
      usage()
   # extract parameters
   extensionFile = None
   outputFile = None
   keyLength = None
   inputFile = args[0] if len(args) > 0 else None
   for opt, arg in opts:
        if opt in ("-h", "--help"):
           usage()
        elif opt in ("-x", "--extension"):
           extensionFile = arg
        elif opt in ("-o", "--output"):
           outputFile = arg
        elif opt in ("-k", "--key-length"):
           keyLength = int(arg)
   # check arguments
   if (extensionFile is None):
       print('extension option is missing\n')
       usage()
   if (outputFile is None):
       print('output option is missing\n')
       usage()
   if (inputFile is None):
       print('input_file is missing\n')
       usage()
  # run the command
   with open(extensionFile, "r") as extensionStream:
        extension = extensionStream.read()
        with open(inputFile, "r") as inputStream:
            data = inputStream.read()
            output = forgeIllegalPayload(data, extension, keyLength)
            with open(outputFile, "w") as outputStream:
                outputStream.write(output)