import sys
from os.path import join, abspath

import subprocess


class Helper:
    def __init__(self):
        
        pass

    def encrypt(self, path_of_xml:str, signature:str, country:str, callback= None):
        # cmd = []
        # if signature == "Slovenian":
        #     cert = resource_path(relative_path="cert\\slovenian.pem")
        #     bin = resource_path(relative_path="Cryptor.exe")
        #     cmd = [bin, f"{path_of_xml}", f"{cert}", "-si"]
        #     callback({"load": f"command: {cmd}"})
        #     print(f"cmd: {cmd}")
        # else:
        #     pass
        
        bin = resource_path(relative_path="Cryptor.exe")
        cert = resource_path(relative_path=f"cert\\{signature}.pem")
        ct = f"-{country.lower()}"
        cmd = [bin, path_of_xml, cert, ct]
        print(cmd)

        try:
            callback({"load": "Encrypting!"})
            subprocess.run(cmd, check=True, stderr=subprocess.DEVNULL, stdout=subprocess.DEVNULL)
            callback({"load": "Encryption Done!"})
        except Exception as e:
            callback({"load": f"Error: {e}"})

def resource_path(relative_path):
        """ Get absolute path to resource, works for dev and for PyInstaller """
        # this is a trick I found on internet.
        try:
            base_path = sys._MEIPASS
        except Exception:
            base_path = abspath(".")

        return join(base_path, relative_path)