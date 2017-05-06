# EC504 Project:Image Segmentation using Network Flow

## Project description
 
This is project is to use Network flow to segment an image from foreground to background. 

## User Instructions
### Installation
git clone https://github.com/zwc662/EC504

### Image segmentation with k-means algorithm
jupyter notebook kmeans_seg.ipynb and follow the instructions inside to segment images with kmeans algorithm and experience the UI.

Anaconda environment is recommendated, or you may result in errors when running the program.


### Image segmentation with network flow algorithm
ecllipse recommended.

Find all the java source files in src/

run ui.java to open GUI to experience image 2-segmentatation and k-segmentation. 

Click File-> Open to browse the images directory to find pgm file as input. You can find all the pgm files in images/

Click File -> 2-segmentation to run image 2-segmentation. After the segmenting finished, the segmented image will replace the original image on the UI panel.

Click File -> k-segmentation to run image k-segmentation. After the segmenting finished, the segmented image will replace the original image on the UI panel.

To run algorithms separately.

Implement ford_fulkerson.java:

 First select some pgm image to feed to the ford_fulkerson algorithm as sourcefile, run:

 $javac ford_fulkerson.java

 $java ford_fulkerson sourcefile outputfile

 sourcefile and outfile are just the filenames, don't add image/ as prefix. All files can be found in images/ directory. If outputfile is not given, the algorithm will output a file named "output_$sourcefile.pgm" in the images/ directory.

run dinic.java or push_relabel.java in the same way


For multi-label image segamentation:

use markov_random_field_opt.java in the same way as ford_fulkerson.java

