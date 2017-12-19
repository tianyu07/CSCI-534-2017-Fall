#!/usr/bin/python
# coding:utf-8  
from Tkinter import *
import httplib, urllib, base64
import os
import httplib, urllib, base64
import json
class Application(Frame):
	def __init__(self, master=None):
		Frame.__init__(self, master)
		self.pack()
		self.createWidgets()

	def createWidgets(self):
		self.helloLabel = Label(self, text='Please type in image address')
		self.helloLabel.pack()
		self.nameInput = Entry(self, width='100')
		self.nameInput.pack()
		self.inputButton = Button(self, text='Dectect Emotion', command = self.detect)
		self.inputButton.pack()
		#self.clear_button = Button(self, text="Clear", command=self.clear_text)
		#self.clear_button.pack()
		self.quitButton = Button(self, text='Quit', command=self.quit)
		self.quitButton.pack()
	def detect(self):
		urlSong = self.nameInput.get()
		self.nameInput.delete(0, 'end')
		headers = {
			# Request headers. Replace the placeholder key below with your subscription key.
			'Content-Type': 'application/json',
			'Ocp-Apim-Subscription-Key': 'cfe4cc88b19048dcbce42593b00bb72f',
		}

		params = urllib.urlencode({
		})
		
		# Replace the example URL below with the URL of the image you want to analyze.
		body = "{ 'url': '" + urlSong + "' }"
		try:
			# NOTE: You must use the same region in your REST call as you used to obtain your subscription keys.
			#   For example, if you obtained your subscription keys from westcentralus, replace "westus" in the 
			#   URL below with "westcentralus".
			conn = httplib.HTTPSConnection('westus.api.cognitive.microsoft.com')
			conn.request("POST", "/emotion/v1.0/recognize?%s" % params, body, headers)
			response = conn.getresponse()
			data = response.read()
			info = json.loads(data)
			out = open("output.txt", 'w')
			out.write("imageId\n")
			out.write("anger\n")
			out.write("joy\n")
			out.write("fear\n")
			out.write("sadness\n")
			out.write("surprise\n")
			out.write("1\n")
			out.write(str(info[0]['scores']['anger']) + "\n")
			out.write(str(info[0]['scores']['happiness'])+ "\n")
			out.write(str(info[0]['scores']['fear'])+ "\n")
			out.write(str(info[0]['scores']['sadness'])+ "\n")
			out.write(str(info[0]['scores']['surprise'])+ "\n")
			out.close()
			
			conn.close()
		except Exception as e:
			print("[Errno {0}] {1}".format(e.errno, e.strerror))
		

app = Application()

app.master.title('Emotion Recognizer')
app.master.geometry('1000x300+300+300')
app.mainloop()

