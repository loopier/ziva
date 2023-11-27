+ Nil {
	sound {|snd|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		var name = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		Ziva.newPlayer(name.asSymbol, snd).debug("New instrument");
		History.eval("~% = Ndef('%')".format(name, name));
		History.eval("~%fx = Ndef('fx_%')".format(name, name));
		code.debug("code");
		name.debug("name");
		^Ndef(name);
	}

	s { |snd| ^this.sound(snd) }

	lfo { |func|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		var name = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		Ndef(name.asSymbol, func);
		History.eval("% = Ndef('%')".format(code, name));
		code.debug("code");
		name.debug("name");
	}

	// fx {|effects|
	// 	var code = thisProcess.interpreter.cmdLine.split($ )[0];
	// 	var name = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
	// 	History.eval("% = Ndef('%', { 'in'.ar(0!2) })".format(code, name));
	// 	^Ndef(name);
	// }
}