from http.server import BaseHTTPRequestHandler, HTTPServer
from http.client import HTTPSConnection
from urllib import parse
import sys

class Server(BaseHTTPRequestHandler):
    # they all use the same code, might as well make it reusable
    def reroute(self, type):
        path = self.path
        headers = self.headers
        body = None

        if(self.headers.get('content-length') is not None):
            body = self.rfile.read(int(self.headers.get('content-length')))
        
        # send an HTTP request to another server and get the response
        conn = HTTPSConnection(headers.get('host'))
        method = type

        conn.request(method, path, body, headers)
        # and get the response back 
        res = conn.getresponse()
        resHeader = res.getheaders()
        size = res.getheader('content-length')
        status = res.status
        res = res.read(int(size))

        conn.close()
        
        # set HTTP response status code
        self.send_response(status)

        # set HTTP reponse headers
        for i in resHeader:
            self.send_header(i[0], i[1])
        self.end_headers()

        # write to alice
        self.wfile.write(res)

        return (path, res)


    # GET request handler 
    def do_GET(self):
        # retrieve the path from the HTTP request
        path, res = self.reroute('GET')

        if(path == '/check'):
            file = open("/shared/flag.txt", 'wb')
            file.write(res)
            file.close()

            sys.exit()

    # POST request handler 
    def do_POST(self):
        self.reroute('POST')
      
    # PUT request handler     
    def do_PUT(self):
        self.reroute('PUT')

    def log_message(self, format, *args):
        return
        
def run_server():
    httpd = HTTPServer(('', 8080), Server)
    httpd.serve_forever()

if __name__ == '__main__':
    run_server()