# Скрипт который находит на изображении бумагу и перспективно её разворачивает
import sys
import cv2

# inputImage = sys.argv[1]
# outputFolder = sys.argv[2]
from utils import show_img

inputImage = "../warp_test.jpg"
outputFolder = "../warp_out"

# if len(sys.argv) < 3:
#     print("No arguments")
#     exit(0)

img = cv2.imread(inputImage)
imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 1)  # ADD GAUSSIAN BLUR
imgThreshold = cv2.Canny(imgBlur, 200, 200)

show_img(imgThreshold)
