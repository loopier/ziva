# Getting Started

## Booting the server
Start the server with default options:

`Ziva.boot;`

## Loading samples
Load some samples by providing a path to the parent directory containing subdirectories with audio files.
As an example we'll be using the great SuperDirt samples set.  You can install the "SuperDirt" quark (see the README) and then load the sounds with:

`Ziva.loadSamples(Platform.userAppSupportDir ++ "/downloaded-quarks/Dirt-Samples")`
