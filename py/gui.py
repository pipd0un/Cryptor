import tkinter as tk
from tkinter import Label, Button, LEFT, W, S, CENTER
from tkinter import filedialog as fd
from threading import Thread
from tkinter.ttk import Combobox


class GUI(tk.Tk):
    def __init__(self, method, icon, screenName: str | None = None, baseName: str | None = None, className: str = "Tk", useTk: bool = True, sync: bool = False, use: str | None = None) -> None:
        super().__init__(screenName, baseName, className, useTk, sync, use)

        self.title("XML Encryptor")
        self.iconbitmap(icon)
        self.geometry("335x190")

        self.method = method

        self.source = ""

        self.greetlabel = Label(self, text="[ INFO ]: FIRST CHOOSE SIGNATURE & COUNTRY!", justify=LEFT)
        self.greetlabel.grid(column=0, row=0, sticky=W)

        self.sourcestate = Label(self, text="[ INFO ]: C:\\{}".format(self.source), justify=LEFT)
        self.sourcestate.grid(column=0, row=1, padx=0, sticky=W)

        self.loadstate = Label(self, text="[ INFO ]: WAITING FOR LOAD!", justify=LEFT, wraplength=300)
        self.loadstate.grid(column=0, row=2, padx=0, sticky=W)

        self.spacelabel = Label(self, text=" ", justify=LEFT)
        self.spacelabel.grid(column=0, row=4, sticky=S)

        self.combobox_label = Label(self, text="Signature:")
        self.combobox_label.grid(row=5, column=0)

        self.country_label = Label(self, text="Country:")
        self.country_label.grid(row=5, column=1)

        self.sign_combobox = Combobox(self, textvariable="Halcom", values=["Halcom"], state="readonly")
        self.sign_combobox.grid(row=6, column=0, padx=10)

        self.country_combobox = Combobox(self, textvariable="SI", values=["SI"], state="readonly", width=4)
        self.country_combobox.grid(row=6, column=1)

        self.interrupt = Button(self, text="Open XML", command=self.job, justify=CENTER)
        self.interrupt.grid(column=0, row=7, sticky=S, pady= 10)


    def job(self):
        file_selected = fd.askopenfilename()

        self.run_thread = Thread(target=self._run)
        
        if file_selected:
            self.source = file_selected
            self.sourcestate.config(text="[ INFO ]: {}".format(f"... {self.source[-30:]}"))
            self.run_thread.start()

        if not file_selected:
            return

        pass

    def _run(self):
        signature = self.sign_combobox.get()
        country = self.country_combobox.get()
        self.interrupt["state"] = tk.DISABLED
        self.method(self.source, signature, country, self.__update_callback)
        self.interrupt["state"] = tk.NORMAL

    def __update_callback(self, message):
        self.loadstate.config(text="[ INFO ]: {}".format(message["load"]),)

