function addIngredient() {
    const ingredientsDiv = document.getElementById('ingredients');
    const ingredientsCount = ingredientsDiv.getElementByTagName('input').length;
    const newIngredient = document.createElement('div');
    newIngredient.innerHtml = `
        <input type="text" name="ingredients[${ingredientCount}]" />
        <button type="button" onclick="removeIngredient(this)">Remove</button>
    `;
    ingredientDiv.appendChild(newIngredient);
}

function removeIngredient(button) {
    button.parentElement.remove();
}