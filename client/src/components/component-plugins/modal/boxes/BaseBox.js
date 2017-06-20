
const BaseBox = {
  props: ['data', 'onCommit'],
  methods: {
    commit(data){
      this.onCommit(data)
    }
  }
}

export default BaseBox
