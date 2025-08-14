package main

import (
	"sort"
)

type Room struct {
	Name     string
	SectorId int
	Checksum string
}

func (r Room) IsReal() bool {

	chars := []rune(r.Name)

	frequency := map[rune]int{}
	for _, char := range chars {
		frequency[char]++
	}
	delete(frequency, '-')

	keys := make([]rune, 0, len(frequency))
	for k := range frequency {
		keys = append(keys, k)
	}
	sort.Slice(keys, func(i, j int) bool {
		return frequency[keys[i]] > frequency[keys[j]] ||
			(frequency[keys[i]] == frequency[keys[j]] && keys[i] < keys[j])
	})

	return string(keys[:5]) == r.Checksum
}

func (r Room) DecryptName() string {
	shift := r.SectorId % 26
	name := r.Name[:len(r.Name)-1] // Exclude the trailing '-'
	decrypted := make([]rune, len(name))

	for i, char := range name {
		if char == '-' {
			decrypted[i] = ' '
		} else {
			decrypted[i] = ((char - 'a' + rune(shift)) % 26) + 'a'
		}
	}

	return string(decrypted)
}
