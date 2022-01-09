import cv2
import numpy as np
from pyzbar.pyzbar import decode

img = cv2.imread('../qr4.png')


for barcode in decode(img):
    myData = barcode.data.decode('utf-8')

    quadratColor = (0, 255, 0)

    pts = np.array([barcode.polygon], np.int32)
    pts = pts.reshape((-1, 1, 2))
    cv2.polylines(img, [pts], True, quadratColor, 5)
    pts2 = barcode.rect

    textColor = (255, 0, 0)
    cv2.text
    cv2.putText(img, myData, (pts2[0], pts2[1]-20), cv2.FONT_HERSHEY_SIMPLEX,
                0.9, textColor, 2)

cv2.imshow('Result', img)
cv2.waitKey(0)
