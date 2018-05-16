import time
start_time = time.time()

def main():
    for j in range(0, 4):
        for i in range(0,999999):
            a = i*i
            print(a)
    print("--- %s seconds ---" % (time.time() - start_time))

if _name_ == '_main_':
    main()