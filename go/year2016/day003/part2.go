package main

func part2() int {
	triangles, _ := parseInput(nil)

	var new_triangles []Triangle

	for i, _ := range triangles {
		if i%3 == 2 {
			new_triangles = append(new_triangles, Triangle{triangles[i].A, triangles[i-1].A, triangles[i-2].A})
			new_triangles = append(new_triangles, Triangle{triangles[i].B, triangles[i-1].B, triangles[i-2].B})
			new_triangles = append(new_triangles, Triangle{triangles[i].C, triangles[i-1].C, triangles[i-2].C})
		}
	}

	valids := 0
	for _, triangle := range new_triangles {
		if triangle.IsValid() {
			valids++
		}
	}
	return valids
}
