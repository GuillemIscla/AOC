use super::ingredient::Ingredient;

pub fn best_mix_ingredients(
    teaspoons: usize,
    ingredients: &[Ingredient],
    calorie_restriction: Option<usize>,
) -> Ingredient {
    let mut current = vec![0; ingredients.len()];
    let mut best_mix = mix(ingredients, &current);

    best_mix_ingredients_internal(
        0,
        teaspoons,
        &mut current,
        &mut best_mix,
        ingredients,
        calorie_restriction,
    );
    best_mix
}

fn best_mix_ingredients_internal(
    pos: usize,
    remaining: usize,
    current: &mut Vec<usize>,
    best_mix: &mut Ingredient,
    ingredients: &[Ingredient],
    calorie_restriction: Option<usize>,
) {
    if pos == current.len() {
        if remaining == 0 {
            let current_mix = mix(ingredients, current);
            if best_mix.score() < current_mix.score() {
                match calorie_restriction {
                    Some(calorie_restriction) => {
                        if current_mix.calories == calorie_restriction {
                            *best_mix = current_mix;
                        }
                    }
                    None => *best_mix = current_mix,
                }
            }
        }
        return;
    }

    for i in 0..=remaining {
        current[pos] = i;
        best_mix_ingredients_internal(
            pos + 1,
            remaining - i,
            current,
            best_mix,
            ingredients,
            calorie_restriction,
        );
    }
}

fn mix(ingredients: &[Ingredient], balance: &[usize]) -> Ingredient {
    let mut mix = Ingredient::new_zero();
    (0..ingredients.len()).for_each(|index| {
        let new_ingredient = ingredients[index].mult(balance[index]);
        mix = mix.add(&new_ingredient);
    });
    mix
}
