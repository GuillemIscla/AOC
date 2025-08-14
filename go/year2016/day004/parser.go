package main

import (
	"bufio"
	"os"
	"regexp"
	"strconv"
)

func parseInput(value *int) ([]Room, error) {
	if value == nil {
		filename := "../../inputs/year2016/day004.txt"
		file, err := os.Open(filename)
		if err != nil {
			return nil, err
		}
		defer file.Close()
		var rooms []Room
		scanner := bufio.NewScanner(file)
		lineNumber := 0
		for scanner.Scan() {
			line := scanner.Text()
			re := regexp.MustCompile(`([a-z,-]+)([0-9]+)\[([a-z]+)\]`)
			matches := re.FindStringSubmatch(line)
			sectorId, _ := strconv.Atoi(matches[2])

			if len(matches) == 4 {
				room := Room{
					Name:     matches[1],
					SectorId: sectorId,
					Checksum: matches[3],
				}
				rooms = append(rooms, room)
			}
			lineNumber++
		}
		return rooms, nil
	} else if *value == 1 {
		return []Room{{Name: "aaaaa-bbb-z-y-x-", SectorId: 123, Checksum: "abxyz"}, {Name: "a-b-c-d-e-f-g-h-", SectorId: 987, Checksum: "abcde"}, {Name: "not-a-real-room-", SectorId: 404, Checksum: "oarel"}, {Name: "totally-real-room-", SectorId: 200, Checksum: "decoy"}}, nil
	} else if *value == 2 {
		return []Room{{Name: "qzmt-zixmtkozy-ivhz", SectorId: 343, Checksum: "abxyz"}}, nil
	}
	return nil, nil
}
