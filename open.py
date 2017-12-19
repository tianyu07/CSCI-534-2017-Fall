#!/usr/bin/python
# coding:utf-8  
import re  
import urllib  
import webbrowser

def getHtml(url):  
    page = urllib.urlopen(url)  
    html = page.read()  
    return html  
    
def getUrl(html):  
    reg = r"(?<=a\shref=\"/watch).+?(?=\")"  
    urlre = re.compile(reg)  
    urllist = re.findall(urlre,html)  
    format = 'https://www.youtube.com/watch%s\n' 
    return urllist[0]
song = open("MusicPlay.txt", 'r')
name = song.readline()
name = name.split()
name = "+".join(name)
html = getHtml('https://www.youtube.com/results?search_query=' + name + '&lclk=short&page=1')
url = getUrl(html)
webbrowser.open('https://www.youtube.com/watch' + url,new=2)

