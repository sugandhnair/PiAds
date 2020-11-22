from PIL import Image
from PIL import Image
from PIL import ImageEnhance
from PIL import ImageFont, ImageDraw
import requests
from io import BytesIO
import cv2
import os
import numpy as np
from datetime import date

filepath_first = "C:/users/sugandh/Pictures/pitest/first"
filepath = "C:/users/sugandh/Pictures/pitest/"

location = 'Vitvara'
today = date.today()
print("Today's date:", today)
baseurl = "http://thantrajna.com/Pi_Ads/getAds.php?location="
getting_ads_url = baseurl+location+"&date="+str(today)
url_results = requests.get(getting_ads_url)
result = url_results.json()
    
if(len(result)>0):
    contact_width = 500
    contact_height = 500
    fontsize = int(contact_width*0.1)
    host = []
    for ele in result:
        image = ele['Image']
        timer = int(ele['Timer']) * int(ele['Count'])
        description = ele['Description']
        print(image)
        print(timer)
        print(description)
        font = ImageFont.truetype("C:/users/sugandh/Downloads/Channel.ttf", fontsize)
        temp = (image,timer,description)
        host.append(temp)
    for pic in host:
        response = requests.get(pic[0])
        text_len = len(pic[2])
        img = Image.open(BytesIO(response.content))
        drawingboard = Image.new(img.mode,(contact_width,contact_height),color='red')
        resized_image = img.resize((contact_width,int(contact_height*0.8)))
        filenumber = 1
        #filepath_first = "C:/users/sugandh/Pictures/pitest/first"
        for slide in range(-500,25,25):
            drawingboard.paste(resized_image,(slide,0))
            drawingboard.save(filepath+str(filenumber)+".png")
            filenumber += 1
        slideends = filenumber
        #first_time = drawingboard.save(filepath+str()+".png")
        drawingboard.paste(resized_image,(0,0))
        d = ImageDraw.Draw(drawingboard)
        box = ImageDraw.Draw(drawingboard)
        drawingboard.save(filepath+str(filenumber)+".png")
        filenumber += 1
        textpos = 0
        
        for i in range(0,int(pic[1])):
            box.rectangle((0,contact_height*0.8,contact_width,contact_height), outline="red",fill='red')
            if(textpos >= contact_width):
                textpos = 0 - text_len*40
            d.text((textpos,int(contact_height - contact_height*0.1)), pic[2], fill='White',font=font)
            textpos += contact_width*0.05
            #filepath = "C:/users/sugandh/Pictures/pitest/"
            saved = drawingboard.save(filepath+str(filenumber)+".png")
            print("Added image")
            filenumber +=1
       
        for i in range(1,filenumber):
            if(i<slideends):
                cv_img = cv2.imread(filepath+str(i)+".png")
                cv2.namedWindow('frame', cv2.WINDOW_FREERATIO)
                cv2.setWindowProperty('frame', cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_FULLSCREEN)
                cv2.imshow('frame',cv_img)
                cv2.waitKey(100)
            else:
                cv_img = cv2.imread(filepath+str(i)+".png")
                cv2.namedWindow('frame', cv2.WINDOW_FREERATIO)
                cv2.setWindowProperty('frame', cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_FULLSCREEN)
                cv2.imshow('frame',cv_img)
                cv2.waitKey(500)
                    
        
        
        cv2.destroyAllWindows()
        
        for i in range(1,filenumber):
            os.remove(filepath+str(i)+".png")
            #os.remove(filepath_first+str(i)+".png")
            
        
else:
    print('No results')
    for waiting in range(0,100000):
        pass