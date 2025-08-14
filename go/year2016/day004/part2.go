package main

func part2() int {
	rooms, _ := parseInput(nil)

	for _, room := range rooms {
		if room.IsReal() {
			decryptedName := room.DecryptName()
			if decryptedName == "northpole object storage" {
				return room.SectorId
			}
		}
	}
	return -1 // Return -1 if not found
}
