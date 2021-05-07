# =============================================
# ===== do not modify the code below ==========
# =============================================

import re

from md5p import md5, padding

def verifyToken(key, hmac):
    result = re.search('\$(.*)\$(.*)', hmac)
    token = result.group(1)
    message = result.group(2)
    return (token == md5(key + message).hexdigest())

if __name__ == "__main__":
   import os, sys, getopt
   def usage():
        print ('Usage:    ' + os.path.basename(__file__) + ' options input_file ')
        print ('Options:')
        print ('\t -k key_file, --key=key_file')
        sys.exit(2)
   try:
      opts, args = getopt.getopt(sys.argv[1:],"hk:",["help", "key="])
   except getopt.GetoptError as err:
      print(err)
      usage()
   # extract parameters
   keyFile = None
   inputFile = args[0] if len(args) > 0 else None
   for opt, arg in opts:
        if opt in ("-h", "--help"):
           usage()
        elif opt in ("-k", "--key"):
           keyFile = arg
   # check arguments
   if (keyFile is None):
       print('key option is missing\n')
       usage()
   if (inputFile is None):
       print('input_file is missing\n')
       usage()
  # run the command
   with open(keyFile, "r") as keyStream:
        key = keyStream.read()
        with open(inputFile, "r") as inputStream:
            data = inputStream.read()
            print(verifyToken(key, data))