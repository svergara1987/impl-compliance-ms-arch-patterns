var expect = require("chai").expect;

describe('test suite', function () {
    before("before", function () {
        // runs once before the first test in this block
        console.log("before runs once before the first test in this block");
    });
    beforeEach("beforeEach", function () {
        // runs before each test in this block
        console.log("beforeEach runs before each test in this block");
    });
    afterEach("afterEach", function () {
        // runs after each test in this block
        console.log("afterEach runs after each test in this block");
    });
    after("after", function () {
        // runs once after the last test in this block
        console.log("after runs once after the last test in this block");
    });
    it('test case #1', function () {
        assert.equal([1, 2, 3].indexOf(4), -1);
        expect(redHex).to.equal("ff0000");
    });
});