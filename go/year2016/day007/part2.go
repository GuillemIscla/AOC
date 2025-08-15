package main

func part2() int {
	ipv7_adresses, _ := parseInput(nil)

	support_ssl := 0

	for _, addr := range ipv7_adresses {
		if addr.SupportsSSL() {
			support_ssl++
		}
	}

	return support_ssl
}
