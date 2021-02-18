##Introduction

Net-Radio is a cross platform audio player with a graphical user interface; designed to store
and play live audio streams broadcast by internet radio stations. The project is still under
active development, however the features mentioned in this README have already been implemented.
This README offers only a bried overview of the projects current state and will be rewritten in 
the latter stages of project to reflect the projects final state.

##Implementation

Audio playback is supported by GStreamer which is capable of constructing custom pipelines on the fly
containing the audio demuxers & decoders required to process any provided audio source. This enables Net-
Radio to support a large variety of container formats (aac,ogg,mp3...) and layer 7 protocols (http, rtsp, mms).

Net-Radio is able to show stream metadata updates in live time, including the details of the current track being
played by the radio station. It is also able to extract the stations "genre" as defined by the station
owner without requiring the user to classify the genre themselves.

OpenCSV is used to implement a persistence mechanism for storing station details and statistics between
sessions. 

The GUI has been constructed using the JafaFX library (enforces MVC). The view is defined in manually
written FXML files. The view and controllers have been split into separate components allowing the view to
remain flexible to future changes. An adapter to the model triggers events which enables all controllers to
respond to the actions of other controllers while maintaining a loose coupling between controllers.

##Dependencies

Environment dependencies:
* GStreamer >= v1.8
* Java SE 15
* Maven

Package dependencies managed by Maven:
* gst1-java-core v1.4 - Java bindings for GStreamer
* OpenCSV v5.3 - Used to store station details between sessions

##Supported Audio Formats

Audio sources offering streams in the following container formats are supported (list is not conclusive):
* MP3
* AAC
* OGG
* WAV

# Screenshots

![general view](https://lemon.codes/git-screenshots/Net-Radio1.png)

![search view](https://lemon.codes/git-screenshots/Net-Radio2.png)