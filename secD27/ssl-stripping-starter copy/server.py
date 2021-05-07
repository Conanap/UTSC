from http.server import BaseHTTPRequestHandler, HTTPServer
from http.client import HTTPSConnection
from urllib import parse
import sys

class Server(BaseHTTPRequestHandler):

    # GET request handler 
    def general(self):
        conn = HTTPSConnection(self.headers.get('host'))
        
        # retrieve the body from the HTTP request
        body = ""
        if (self.headers.get('content-length')):
            body = self.rfile.read(int(self.headers.get('content-length')))
        # send an HTTP request to another server and get the response
        
        # get method
        method = self.command
        conn.request(method, self.path, body, self.headers)
        # and get the response back 
        res = conn.getresponse()
        data = res.read()
        
        # set HTTP response status code and body
        self.send_response(res.status)
        # set HTTP reponse headers
        self.send_header("content-type", "text/html")
        self.send_header("content-length", res.headers.get("content-length"))
        self.end_headers()
        self.wfile.write(data)
        
        # write in body
        if (self.path == "/check"):
            file = open("/shared/flag.txt", "wb")
            file.write(data)
            file.close()
            sys.exit(0)

        conn.close()

    # POST request handler 
    def do_GET(self):
        self.general()

    # POST request handler 
    def do_POST(self):
        self.general()
      
    # PUT request handler     
    def do_PUT(self):
        self.general()  
        
def run_server():
    httpd = HTTPServer(('', 8080), Server)
    httpd.serve_forever()



if __name__ == '__main__':
    run_server()
