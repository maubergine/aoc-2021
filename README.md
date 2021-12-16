Marius' Advent of Code 2021
===========================

For all your poorly realised solution needs...

If you have gource installed you can have fun with this command:

`gource --key -s 1 --default-user-image santa.png`

Or to export to movie (with ffmpeg):

`gource -1280x720 --key -s 1 --multi-sampling --stop-at-end --default-user-image santa.png --output-ppm-stream - --output-framerate 30 | ffmpeg -y -r 30 -f image2pipe -vcodec ppm -i - -vcodec libx264 -preset ultrafast -pix_fmt yuv420p -crf 1 -threads 0 -bf 0 gource.mp4`