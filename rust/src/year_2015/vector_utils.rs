pub fn get_permutations(n: usize) -> Vec<Vec<usize>> {
    get_permutations_internal((0..n).collect())
}

fn get_permutations_internal(to_permute: Vec<usize>) -> Vec<Vec<usize>> {
    if to_permute.len() == 1 {
        vec![to_permute]
    } else {
        let mut permutations: Vec<Vec<usize>> = vec![];
        for i in 0..to_permute.len() {
            let new_permutations: Vec<Vec<usize>> =
                get_permutations_internal([&to_permute[0..i], &to_permute[i + 1..]].concat())
                    .iter()
                    .map(|tail| [vec![to_permute[i]], tail.to_vec()].concat())
                    .collect();
            new_permutations
                .iter()
                .for_each(|permutation| permutations.push(permutation.to_vec()));
        }
        permutations
    }
}
