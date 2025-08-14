package main

func part1() int {
	triangles, _ := parseInput(nil)

	valids := 0

	for _, triangle := range triangles {
		if triangle.IsValid() {
			valids++
		}
	}
	return valids
}
