from typing import Optional

import cv2


def annotateLabel(image, point1, point2, label, overwrite: bool = True) -> Optional:
    if not overwrite:
        result = image.copy()
    else:
        result = image
    cv2.rectangle(result, point1, point2,
                  (0, 255, 0), 2)
    font = cv2.FONT_HERSHEY_SIMPLEX
    cv2.putText(result, label,
                (point1[0], point1[1] - 10), font,
                1, (0, 0, 255), 2, cv2.LINE_4)
    if not overwrite:
        return result
    else:
        return None
