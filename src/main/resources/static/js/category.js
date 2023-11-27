function addCategory() {
    let categoryName = prompt("새 카테고리의 이름을 입력하세요:");
    if(categoryName) {
        $.ajax({
            type: "POST",
            url: "/api/category",
            data: JSON.stringify({ name: categoryName }),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.status == 200) {
                alert("카테고리가 추가되었습니다.");
                location.reload(); // 페이지를 새로 고침하여 새 카테고리를 표시
            } else {
                alert("카테고리 추가에 실패하였습니다.");
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    } else {
        alert("카테고리 이름을 입력해야 합니다.");
    }
}
function deleteCategory(categoryId) {
    if(confirm('해당 카테고리를 삭제하시겠습니까?')) {
        $.ajax({
            type: "DELETE",
            url: `/api/category/${categoryId}`,
            dataType: "json"
        }).done(function(resp) {
            if(resp.status == "200") {
                $(`#category-item-${categoryId}`).remove();
                alert("카테고리가 삭제되었습니다.");
                location.reload();
            } else {
                alert("카테고리 삭제에 실패하였습니다.");
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
}
