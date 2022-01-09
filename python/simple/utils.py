import cv2


def show_img(img):
    wantedWidth = 700

    height = img.shape[0]
    width = img.shape[1]
    wantedHeight = round((wantedWidth / width) * height)

    resized = cv2.resize(img.copy(), (wantedWidth, wantedHeight))
    cv2.imshow("Show", resized)
    cv2.waitKey(0)
