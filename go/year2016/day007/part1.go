package main

func part1() int {
	ipv7_adresses, _ := parseInput(nil)

	support_tls := 0

	for _, addr := range ipv7_adresses {
		if addr.SupportsTLS() {
			support_tls++
		}
	}

	return support_tls
}
