package main

func part1() int {
	rooms, _ := parseInput(nil)

	idSectorSum := 0
	for _, room := range rooms {
		if room.IsReal() {
			idSectorSum += room.SectorId
		}
	}
	return idSectorSum
}
