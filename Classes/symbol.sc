+Symbol {
	s { |snd|
		var key = (\track++this.asString.replace("/","")).asSymbol;
		Ziva.proxyspace.at(key)[0] = {};
		^Ziva.proxyspace.at(key);
	}

	asHex {
		^this.asString.asHexIfPossible;
	}

	chord { | inversion=0 | ^Ziva.chord(this, inversion) }

	// doesNotUnderstand{ |selector ... args|
	// 	var key = (\track++this).asSymbol;
	// 	var isNdef = false;
	// 	if( Ziva.proxyspace.keys.includes(key)) {
	// 		Ziva.proxyspace.at(key).pause;
	// 	}{
	// 		super.doesNotUnderstand(selector, args);
	// 	};
	// }
}