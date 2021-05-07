lines_subset = lines[:100]
review_strings = []
for line in lines_subset:
  # change line into a dict
  temp = json.loads(line)
  # grab text from json dict
  txt = temp['text']

  # split the paragraph into sentences, assuming sentence always ends with .
  # gives list of sentences
  txts = txt.split('.')

  # for each sentence
  for sen in txts:
    # split at space to get words
    # end up with list of words
    review_string = sen.split(' ')
    # add it to the list of reviews
    review_strings.append(review_string)
