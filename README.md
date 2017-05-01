##EC504 Project
#Image Segmentation


#Project description
 
This is project is to use Network flow to segment an image from foreground to background. 


#User Instructions

Find all the java source files in src/

Find all the pgm files in images/

To implement ford_fulkerson.java:

First select some pgm image to feed to the ford_fulkerson algorithm as sourcefile, run:

$javac ford_fulkerson.java

$java ford_fulkerson sourcefile outputfile

sourcefile and outfile are just the filenames, don't add image/ as prefix.

For multi-label image segamentation:

use markov_random_field_opt.java in the same way

