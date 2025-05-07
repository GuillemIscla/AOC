#[derive(Debug)]
pub struct PackageDivision {
    pub weighted_set: Vec<usize>,
    pub rest: Vec<usize>,
}

pub fn get_package_divisions(
    number_of_packages: Option<usize>,
    weight: usize,
    packages: &[usize],
) -> Vec<PackageDivision> {
    match number_of_packages {
        Some(number_of_packages) => {
            if weight == 0 || number_of_packages == 0 {
                if weight == 0 && number_of_packages == 0 {
                    return vec![PackageDivision {
                        weighted_set: Vec::new(),
                        rest: packages.to_vec(),
                    }];
                } else {
                    return Vec::new();
                }
            }
        }
        None => {
            if weight == 0 {
                return vec![PackageDivision {
                    weighted_set: Vec::new(),
                    rest: packages.to_vec(),
                }];
            }
        }
    }

    packages
        .iter()
        .enumerate()
        .flat_map(|(index, &package)| {
            if package <= weight {
                get_package_divisions(
                    number_of_packages
                        .map(|number_of_packages| number_of_packages.saturating_sub(1)),
                    weight.saturating_sub(package),
                    &packages[index + 1..],
                )
                .iter_mut()
                .map(|PackageDivision { weighted_set, rest }| {
                    weighted_set.push(package);
                    let weighted_set = weighted_set.to_vec();
                    let rest = packages[0..index]
                        .iter()
                        .chain(rest.iter())
                        .copied()
                        .collect();
                    PackageDivision { weighted_set, rest }
                })
                .collect()
            } else {
                Vec::new()
            }
        })
        .collect::<Vec<PackageDivision>>()
}

pub fn get_quantum_entanglement(packages: &[usize]) -> usize {
    packages.iter().product()
}
