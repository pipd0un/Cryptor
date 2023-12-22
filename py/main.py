from gui import GUI
from utils import Helper, resource_path

if __name__ =="__main__":
    helper = Helper()

    icon = resource_path("oregon.ico")
    gui = GUI(method=helper.encrypt, icon=icon)
    gui.mainloop()
    
    