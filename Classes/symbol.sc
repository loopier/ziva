+Symbol {
	s { |snd|
		var key = (\track++this).asSymbol;
		Ziva.proxyspace.at(key).pause;
	}

	// *new {
	// 	Ziva.proxyspace.at(this).pause;
	// }

	doesNotUnderstand{ |selector ... args|
		var key = (\track++this).asSymbol;
		var isNdef = false;
		if( Ziva.proxyspace.keys.includes(key)) {
			Ziva.proxyspace.at(key).pause;
		}{
			super.doesNotUnderstand(selector, args);
		};
	}
}