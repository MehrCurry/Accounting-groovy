package accounting

class PostingService {
    def post(Posting posting) {
        posting.post().save()
    }
}
