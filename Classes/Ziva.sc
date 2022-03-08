// Live coding in SuperCollider made easy.

// A general class to manage live coding resources.
// Some parts are inspired by SuperDirt by Julian Rohrhuber

// (C) 2022 Roger Pibernat

// Ziva is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 2 of the License, or (at your
// option) any later version.

// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

Ziva {
	classvar <> server;
	classvar <> samplesPath;

	*start { |inputChannels = 2, outputChannels = 2, server = nil|
		^this.boot(inputChannels, outputChannels, server);
	}

	*boot { |inputChannels = 2, outputChannels = 2, server = nil,
		numBuffers = 16, memSize = 32, maxNodes = 32, samplesPath = nil|
		server = server ? Server.default;
		this.server = server;
		this.serverOptions(this.server, inputChannels, outputChannels, numBuffers, memSize, maxNodes);
		this.server.waitForBoot{
			ZivaEventTypes.new;
			this.loadSounds;
		};
		^this.server;
	}

	*serverOptions { |server = nil, inputChannels = 2, outputChannels = 2, numBuffers = 16, memSize = 32, maxNodes = 32|
		server = server ? Server.default;
		server.options.numBuffers = 1024 * numBuffers; // increase this if you need to load more samples
		server.options.memSize = 8192 * memSize; // increase this if you get "alloc failed" messages
		server.options.maxNodes = 1024 * maxNodes;
		server.options.numInputBusChannels = inputChannels;
		server.options.numOutputBusChannels = outputChannels;
	}

	*scope { |alwaysOnTop = true|
		server.scope(2).style_(2)
		.window.bounds_(Rect( 145 + 485, 0, 200, 250))
		.alwaysOnTop_(alwaysOnTop);
	}

	/// \brief load samples
	*loadSounds {
		"loading sounds".debug;
		this.loadSynths;
		this.loadSamples;
	}

	/// \brief load synthdefs
	*loadSynths { |path|
		// load synthdesclib
		// "loading synths".debug;
		var filePaths;
		path = path ?? { "../synths".resolveRelative };
		filePaths = pathMatch(standardizePath(path +/+ "*"));
		filePaths.do { |filepath|
			if(filepath.splitext.last == "scd") {
				(dirt:this).use { filepath.load }; "loading synthdefs in %\n".postf(filepath)
			}
		}
	}

	/// \brief Return a list of all compiled SynthDef names
	*synthDefList {
		var names = SortedList.new;

		SynthDescLib.global.synthDescs.do { |desc|
			if(desc.def.notNil) {
				// Skip names that start with "system_"
				if ("^[^system_|pbindFx_]".matchRegexp(desc.name)) {
					names.add(desc.name);
				};
			};
		};

		^names;
	}

	/// \brief	Load samples from directory.
	/// \description
	///  	Load samples from subfolders in the given path.
	///  	Samples will be accessed with folder name and index
	///  	A directory with symlinks should work.
	/// \returns Dictionary
	*loadSamples { arg path, server = nil;
		try {
			var d = Dictionary.new;
			server = server ? this.server ? Server.default;
			PathName(path).entries.do { |item, i|
				// d.add(item.folderName -> this.loadSamplesArray(item.fullPath, server));
				currentEnvironment.put(item.folderName.asSymbol, this.loadSamplesArray(item.fullPath, server));
			};
			this.listLoadedSamples;
			// ^d;
		} {
			"ERROR: Sample path not set.  Use .loadSamples(PATH).".postln;
		}
	}

	/// \brief 	load samples from a directory.
	/// \description 	Samples must be files within the given path.
	/// \returns an Array
	*loadSamplesArray { arg path, server = nil;
		var a = Array.new;
		server = server ? this.server ? Server.default;
		PathName(path).entries.do({ |item, i|
			// item.fullPath.postln;
			a = a.add(Buffer.read(server, item.fullPath));
		});
		^a;
	}

	/// \brief	Loads samples from an array of paths into the environment which
	/// 		will make them available as variables with ~ (~folderName).
	/// \returns nothing, variables will be available with 'currentEnvironment[NAME]'
	*loadSamplesAsSymbols { arg paths = [], s = Server.default;
		paths.do { |path|
			var name  = PathName(path).folderName;
			currentEnvironment.put(name.asSymbol, Loopier.loadSamplesArray(path, s));
		};
	}

	*listLoadedSamples {
		currentEnvironment.keys.asArray.sort.do{|k|
			"% (%)".format(k, currentEnvironment[k].size).postln;
		}
	}

	/// \brief list synth names
	*synths {
		this.synthDefList.collect(_.postln);
	}

	/// \brief post synths and samples
	*sounds {
		// list synths
		// list samples - name(items)
		"listing sounds".debug;
	}

	/// \brief return a list of the controls for the given synth
    *synthControls { |synth|
        var controls = List();
        SynthDescLib.global.at(synth).controls.do{ |ctl|
            controls.add([ctl.name, ctl.defaultValue]);
        }
        ^controls
    }

	/// \brief list available methods
	// *help { |class|
	// 	class = class ? Pchain;
	// 	class.methods.collect(_.postln);
	// }

	*controls { |synth|
        "% controls".format(synth).postln;
        this.synthControls(synth).collect(_.postln)
	}
}