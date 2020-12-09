package com.example.finalproject;


import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

//public class Classifier implements ImageAnalysis.Analyzer {
public class Classifier{
    ImageLabeler imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

}
